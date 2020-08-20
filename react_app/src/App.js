import React from 'react';
import logo from './logo.svg';
import './App.css';
import Header from './components/Header/header.js';
import Footer from './components/Footer/footer.js'; 
import Login from './components/main/login.js';

function App() {
  return (
      <div className="App">
          <Header/>
          <Login/>
          <Footer/>
    </div>
  );
}

export default App;
