import React from 'react';
import './App.css';
import Main from './components/main/main.js';
import { useHistory } from 'react-router-dom';

function App() {
  return (
      <div className="App">
            <Main history={useHistory()}/>
    </div>
  );
}

export default App;
