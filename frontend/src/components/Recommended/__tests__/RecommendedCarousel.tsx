// import dependencies
import React from 'react'
// import react-testing methods
import { render, waitFor, screen } from '@testing-library/react'
// add custom jest matchers from jest-dom
import '@testing-library/jest-dom'
import axios from 'axios';
// the component to test
import RecommendedCarousel from 'src/components/Recommended/RecommendedCarousel';

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
    render(<RecommendedCarousel listings={[]} />)

    await waitFor(() => {
      expect(
        screen.getByText("No listings found")
      ).toBeInTheDocument();
    });
  });

  test('one listing', async () => {
    (axios.get as jest.Mock).mockResolvedValue({
      data: [{
        cardName: "Test Card",
        author: "test",
        price: "test",
        image: "test",
        alt: "Test Alt"
      }]
    })
    const res = await axios.get('/listing');
    render(<RecommendedCarousel listings={res.data} />)

    await waitFor(async () => {
      const listings = await screen.findAllByAltText("Test Alt")
      expect(listings).toHaveLength(1);
    });
  });

})

