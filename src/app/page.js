'use client';
import { useEffect } from "react";

import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();
  useEffect(()=>{
    setTimeout(()=>{
      router.push("/1");
    },3000)
  }, [router])
    
  return (
    <>
   <div className="body pt-0">
      <img src="/assets/logo.jpg" width={150} />
   </div>
</>
  );
}
