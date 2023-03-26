// import dependencies
import React from 'react'
// import react-testing methods
import { render, waitFor, screen } from '@testing-library/react'
// add custom jest matchers from jest-dom
import '@testing-library/jest-dom'
import axios from 'axios';
// the component to test
import RecommendedCarousel from 'src/components/Recommended/RecommendedCarousel';
import { BrowserRouter } from 'react-router-dom';

jest.mock("axios");

const resize = (width: number) => {
  Object.defineProperty(window, 'innerWidth', {
    get: () => width,
  });

  Object.defineProperty(window.HTMLElement.prototype, 'offsetWidth', {
    get: () => width,
  });

  window.dispatchEvent(new Event('resize'));
};

describe('Carousel', () => {
  beforeAll(() => resize(500)); // mobile

  test('no listing found', async () => {
    render(
      <BrowserRouter>
        <RecommendedCarousel listings={[]} />
      </BrowserRouter>
    )

    await waitFor(() => {
      expect(
        screen.getByText("No recommendations found")
      ).toBeInTheDocument();
    });
  });

  test('one listing', async () => {
    (axios.get as jest.Mock).mockResolvedValue({
      data: [{
        cardName: "Test Card",
        listingId: 1,
        author: "test",
        price: "test",
        alt: "test"
      }]
    })
    const res = await axios.get('/listing');
    render(
      <BrowserRouter>
        <RecommendedCarousel listings={res.data} />
      </BrowserRouter>
    )
  
    await waitFor(async () => {
      const listings = await screen.findAllByText("Test Card")
      expect(listings).toHaveLength(1);
    });
  });

})

