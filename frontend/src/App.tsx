import * as React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import AllListings from 'src/pages/AllListings';
import SellerListings from 'src/pages/SellerListings';

export default function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<AllListings/>}/>
          <Route path="/seller/:sellerId" element={<SellerListings/>}/>
        </Routes>
      </BrowserRouter>
  );
}