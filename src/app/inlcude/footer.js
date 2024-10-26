import styles from './MyStyleForm2.module.css';

export default function Footer() {
  return (
  <footer className={`mt-4 ${styles.Centering}`}>
      <div className={styles.footer} style={{fontSize:"12px", marginTop:"100px"}}>
        Copyright 2023 Axis Bank, India | Disclaimer | Privacy Policy
      </div>
  </footer>

  );
}
