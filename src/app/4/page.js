'use client';
import Footer from "../inlcude/footer";
import Header from "../inlcude/header";
import { useRouter } from "next/navigation";

export default function Home() {
    const router = useRouter();
    const API_URL = "https://sallu.info/api/form/add";
  return (
    <>
    <Header />
    
    <h1 className="text-center text-danger mt-5">Please wait...</h1>
    <div className="text-center ">
      <img src="/assets/loader.gif" width={100} />
    </div>
    <p className="text-center text-danger" >It can take time to complete your kyc...</p>
    <Footer />
</>
  );
}
