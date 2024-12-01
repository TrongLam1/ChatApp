import HomePageComponent from "@/components/homePage/homePageComponent";
import { auth } from "../../auth";
import { CountRequestFriends, GetListFriends, GetListRequestFriends } from "./api/friendshipApi";
import { GetListGroupsOfUser } from "./api/groupApi";

export default async function Home({ searchParams }: {
  searchParams: { [key: string]: string | string[] | undefined }
}) {

  const session = await auth();
  const user = session?.user.user;
  const token = session?.user.token;

  const tab = searchParams.tab;
  let listContacts = null;

  const countRequest = await CountRequestFriends(token);

  if (tab === 'groups') {
    const res = await GetListGroupsOfUser(token);
    listContacts = res.data;
  } else if (tab === 'accepts') {
    const res = await GetListRequestFriends(token);
    listContacts = res.data;
  } else {
    const res = await GetListFriends(token);
    listContacts = res.data;
  }

  return (
    <HomePageComponent tab={tab} user={user} token={token}
      countRequest={countRequest.data} listContacts={listContacts} />
  );
}
