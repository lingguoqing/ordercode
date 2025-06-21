import React from 'react';
import { useLocation, Link } from 'react-router-dom';
import styles from './Detail.module.css';

const Detail = () => {
  const location = useLocation();
  const { movie } = location.state || {};

  if (!movie) {
    return (
      <div className={styles.detailContainer}>
        <p>电影信息未找到。</p>
        <Link to="/">返回首页</Link>
      </div>
    );
  }

  return (
    <div className={styles.detailContainer}>
      <div className={styles.topSection}>
        <div className={styles.posterImage}>
          <img src={movie.image} alt={movie.name} />
        </div>
        <div className={styles.topDetails}>
          <h2>{movie.name}</h2>
          <p>{movie.showTime.replace('上映：', '')}</p>
          <p>{movie.type.replace('类型: ', '')}</p>
          <p>{movie.director}</p>
          <p>{movie.actor}</p>
        </div>
      </div>

      <div className={styles.descriptionSection}>
        <p>{movie.desc}</p>
      </div>

      <div className={styles.posterSection}>
        <h3>电影海报</h3>
        <div className={styles.posterList}>
          {movie.posterlist.map((poster, index) => (
            <img key={index} src={poster} alt={`${movie.name} poster ${index + 1}`} />
          ))}
        </div>
      </div>
       <div className={styles.backButtonContainer}>
          <Link to="/">
              <button className={styles.backButton}>返回首页</button>
          </Link>
      </div>
    </div>
  );
};

export default Detail; 