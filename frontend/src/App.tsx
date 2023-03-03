import React, { useEffect } from 'react';
import logo from './logo.svg';
import './App.css';
import { Link } from '@mui/material';
import { getGoogleUrl } from './utils/getGoogleURL';

function App() {

  function getParameterByName(name:string, url = window.location.href) {
    name = name.replace(/[[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
  }

  useEffect(()=>{

     const url = window.location.href
     const hasToken = url.includes('token=')
 
    
     if (hasToken) {
      var token = getParameterByName('token'); // "lorem"
      
      if(token) {
        localStorage.setItem("token", token);
      }

      const headers = new Headers({
        "Content-Type": "application/json",
      });

      if (localStorage.getItem("token")) {
        headers.append("Authorization", "Bearer " + localStorage.getItem("token"));
      }

      fetch("http://localhost:8080/api/v1/demo/profile", {
        method: 'GET',
        headers:headers
      })
        .then((response) => {
          if(response.status >= 400){
            throw new Error()
          }
          return response.json()
        })
        .then((data) => {
          console.log(data)
        })
     }
  },[])
  
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <Link href={(getGoogleUrl())}>This is the Google Button</Link>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
      
    </div>
  );
}

export default App;
