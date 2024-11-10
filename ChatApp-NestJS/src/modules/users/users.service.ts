import { BadRequestException, Body, Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { hashPasswordHelper } from 'src/helpers/utils';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { User } from './schemas/user.schema';
import { CloudinaryService } from 'src/cloudinary/cloudinary.service';

@Injectable()
export class UsersService {
  constructor(
    @InjectModel(User.name)
    private readonly userModel: Model<User>,
    private readonly cloudinaryService: CloudinaryService
  ) { }

  private isEmailExist = async (email: string) => {
    const existEmail = await this.userModel.exists({ email: email });
    if (existEmail) return true;
    return false;
  }

  async register(@Body() createUserDto: CreateUserDto) {
    const { username, email, password, provider } = createUserDto;

    const checkEmail = await this.isEmailExist(email);
    if (checkEmail) throw new BadRequestException(`Email ${email} đã được sử dụng.`);

    const hashPassword = await hashPasswordHelper(password);

    let user;

    if (provider !== null) {
      user = await this.userModel.create({
        name: username, email, password: hashPassword, accountType: provider
      });
    } else {
      user = await this.userModel.create({
        name: username, email, password: hashPassword
      });
    }

    return {
      id: user._id, email: user.email, username: user.name
    };
  }

  async resetPassword(user, newPassword: string) {
    if (user.accountType !== null) return `Tài khoản ${user.accountType} không thể đổi mật khẩu.`;

    const newPasswordHash = await hashPasswordHelper(newPassword);
    await this.userModel.findByIdAndUpdate(
      { _id: user._id },
      { password: newPasswordHash }
    );

    return "Reset password successfully.";
  }

  async logout(user) {
    user = await this.userModel.findByIdAndUpdate(
      { _id: user._id },
      { refreshToken: null });
    return { id: user._id.toString(), username: user.name, refreshToken: user.refreshToken };
  }

  async findOneByEmail(email: string) {
    return await this.userModel.findOne({ email });
  }

  async getProfile(req: any) {
    return await this.userModel
      .findById({ _id: req.user.userId })
      .select('name email phone imageUrl');
  }

  async saveRefreshToken(user, refreshToken: string) {
    user = await this.userModel.findByIdAndUpdate(
      { _id: user._id },
      { refreshToken: refreshToken },
      { new: true }
    );
    return user.refreshToken;
  }

  async findUsersByNameContains(current: number, pageSize: number, sort: string, name: string) {
    if (!current || current == 0) current = 1;
    if (!pageSize || current == 0) pageSize = 10;

    const sortOrder: 'ASC' | 'DESC' = sort === 'DESC' ? 'DESC' : 'ASC';

    const totalItems = (await this.userModel.find({ name })).length;
    const totalPages = Math.ceil(totalItems / pageSize);
    const skip = (current - 1) * pageSize;

    const result = await this.userModel
      .find({ name })
      .limit(pageSize)
      .skip(skip)
      .select('-password')
      .sort(sortOrder);

    return { result, totalItems, totalPages };
  }

  async updateUser(req: any, @Body() updateUserDTO: UpdateUserDto) {
    const { username, phone } = updateUserDTO;

    const updateFields: any = {};
    if (username) updateFields.name = username;
    if (phone) updateFields.phone = phone;

    const user = await this.userModel.findByIdAndUpdate(
      req.user.userId,
      updateFields,
      { new: true, runValidators: true }
    );

    if (!user) throw new NotFoundException("Không tìm thấy thông tin người dùng.");

    return {
      id: user._id,
      username: user.name,
      phone: user.phone,
      email: user.email
    };
  }

  async changeAvatar(req, file: Express.Multer.File) {
    const files = await this.cloudinaryService.uploadFile(file);
    const user = await this.userModel.findByIdAndUpdate(
      { _id: req.user.userId },
      {
        imageId: files.public_id,
        imageUrl: files.url
      },
      { new: true }
    );

    return {
      id: user._id,
      username: user.name,
      phone: user.phone,
      email: user.email,
      avatar: user.imageUrl
    }
  }
}
