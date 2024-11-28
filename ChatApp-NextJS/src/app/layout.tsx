import type { Metadata } from "next";
import { Roboto } from "next/font/google";
import { config } from "@fortawesome/fontawesome-svg-core";
import '@fortawesome/fontawesome-svg-core/styles.css';
import "bootstrap/dist/css/bootstrap.min.css";
import './globals.css';
import 'react-toastify/dist/ReactToastify.css';
import { Bounce, ToastContainer } from "react-toastify";
import NextAuthWrapper from "@/libs/next.auth.wrapper";
import { TabProvider } from "@/providers/tabProvider";
import { ContactObjectProvider } from "@/providers/contactObjectProvider";

config.autoAddCss = false;

const roboto = Roboto({
  weight: '400',
  subsets: ['latin'],
})

export const metadata: Metadata = {
  title: "Chat App",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={roboto.className}>
        <NextAuthWrapper>
          <TabProvider>
            <ContactObjectProvider>
              <div className="app-container">
                {children}
              </div>
            </ContactObjectProvider>
          </TabProvider>
        </NextAuthWrapper>
        <ToastContainer
          position="top-right"
          autoClose={2000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="light"
          transition={Bounce}
        />
      </body>
    </html>
  );
}
