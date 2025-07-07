import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home/Home';
import Detail from './pages/Detail/Detail';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/detail/:movieId" element={<Detail />} />
      </Routes>
    </div>
  );
}

export default App; 