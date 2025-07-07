import React, { useState, useEffect } from 'react';
import axios from 'axios';
import MovieItem from '../MovieItem/MovieItem';
import styles from './MovieList.module.css';

const MovieList = () => {
  const [movies, setMovies] = useState([]);

  useEffect(() => {
    axios.get('/data.json')
      .then(response => {
        setMovies(response.data.data);
      })
      .catch(error => {
        console.error("Error fetching the movie data:", error);
      });
  }, []);

  return (
    <div className={styles.movieList}>
      {movies.map(movie => (
        <MovieItem key={movie.movieId} movie={movie} />
      ))}
    </div>
  );
};

export default MovieList; 