import styles from '../LoginForm.module.css';

export default function Footer() {
  return (
  <footer className="mt-4 text-center">
      <div className={styles.footer} style={{fontSize:"12px", marginTop:"100px"}}>
        Copyright 2023 Axis Bank, India | Disclaimer | Privacy Policy
      </div>
  </footer>

  );
}
