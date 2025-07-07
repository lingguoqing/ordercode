import React, { useState } from 'react';
import styles from './Header.module.css';

const Header = () => {
  const [activeTab, setActiveTab] = useState('hot');

  return (
    <div className={styles.header}>
      <div
        className={`${styles.tab} ${activeTab === 'hot' ? styles.active : ''}`}
        onClick={() => setActiveTab('hot')}
      >
        正在热映
      </div>
      <div
        className={`${styles.tab} ${activeTab === 'soon' ? styles.active : ''}`}
        onClick={() => setActiveTab('soon')}
      >
        即将上映
      </div>
    </div>
  );
};

export default Header; 