import React from 'react';
import Header from '../../components/Header/Header';
import Banner from '../../components/Banner/Banner';
import MovieList from '../../components/MovieList/MovieList';
import styles from './Home.module.css';

const Home = () => {
  return (
    <div className={styles.homeContainer}>
      <Header />
      <Banner />
      <MovieList />
    </div>
  );
};

export default Home; 