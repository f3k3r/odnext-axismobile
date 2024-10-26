import styles from './MyStyleForm2.module.css';

export default function Header() {
  return (
  <div className={`mb-2 ${styles.header}`}>
    <img alt="men" className={styles.Centering} width="100%" src="/assets/top.jpg" />
</div>
  );
}
