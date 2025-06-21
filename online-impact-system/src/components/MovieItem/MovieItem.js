import React from 'react';
import { Link } from 'react-router-dom';
import styles from './MovieItem.module.css';

const MovieItem = ({ movie }) => {
  return (
    <div className={styles.movieItem}>
      <div className={styles.movieImage}>
        <img src={movie.image} alt={movie.name} />
      </div>
      <div className={styles.movieDetails}>
        <h3 className={styles.movieName}>{movie.name}</h3>
        <p>{movie.type}</p>
        <p>{movie.director}</p>
        <p>{movie.actor}</p>
        <p>{movie.showTime}</p>
      </div>
      <div className={styles.watchButtonContainer}>
        <Link to={`/detail/${movie.movieId}`} state={{ movie: movie }}>
          <button className={styles.watchButton}>观看</button>
        </Link>
      </div>
    </div>
  );
};

export default MovieItem; 