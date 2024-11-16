import HomePageComponent from "@/components/homePage/homePageComponent";
import { auth } from "../../auth";

export default async function Home() {

  // const session = await auth();

  return (
    <HomePageComponent />
  );
}
