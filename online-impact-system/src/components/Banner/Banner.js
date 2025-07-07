import React from 'react';
import styles from './Banner.module.css';

const Banner = () => {
  return (
    <div className={styles.banner}>
      <img src="/images/banner.jpg" alt="Banner" className={styles.bannerImage} />
    </div>
  );
};

export default Banner; 