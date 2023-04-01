import React from "react";
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import { Grow, Paper, Typography } from "@mui/material";
import { RECOMMENDATION } from "src/constants/endpoints";
import { useHttpClient } from "src/hooks/http-hook";
import { UserLoginContext } from "src/context/userLoginContext";
import ListingCard from "src/components/Listing/ListingCard";
import { buildListings } from "src/utils/listings";
import "src/styles/Carousel.css";

export interface IRecommendedCarouselProps {}

export default function RecommendedCarousel(props: IRecommendedCarouselProps) {
  const { sendRequest } = useHttpClient();
  const { isLoggedIn, profile } = React.useContext(UserLoginContext);
  const [recommendedListings, setRecommendedListings] = React.useState([]);
  React.useEffect(() => {
    const getRecommendedItems = async () => {
      if (isLoggedIn) {
        let recommendations = await sendRequest(
          RECOMMENDATION,
          "GET",
          {},
          `/${profile.id}`
        );
        recommendations = buildListings(recommendations);
        setRecommendedListings(recommendations);
      }
    };
    getRecommendedItems();
  }, [sendRequest, isLoggedIn, profile]);
  const responsive = {
    superLargeDesktop: {
      // the naming can be any, depends on you.
      breakpoint: { max: 4000, min: 3000 },
      items: 5,
    },
    desktop: {
      breakpoint: { max: 3000, min: 1024 },
      items: 3,
    },
    tablet: {
      breakpoint: { max: 1024, min: 464 },
      items: 2,
    },
    mobile: {
      breakpoint: { max: 464, min: 0 },
      items: 1,
    },
  };
  const RightArrow = (arrowProps: any) => {
    const { carouselState, children, ...restArrowProps } = arrowProps;
    return (
      <ArrowForwardIosIcon
        fontSize="large"
        className="arrow right"
        {...restArrowProps}
      />
    );
  };
  const LeftArrow = (arrowProps: any) => {
    const { carouselState, children, ...restArrowProps } = arrowProps;
    return (
      <ArrowBackIosNewIcon
        fontSize="large"
        className="arrow left"
        {...restArrowProps}
      />
    );
  };
  if (recommendedListings.length === 0) {
    return (
      <Typography variant="h4" align="center" color="text.secondary" mt={2}>
        No recommendations found
      </Typography>
    );
  }
  return (
    <Carousel
      responsive={responsive}
      draggable={false}
      autoPlay={true}
      containerClass="carousel"
      itemClass="carousel-item"
      rtl={false}
      customRightArrow={<RightArrow />}
      customLeftArrow={<LeftArrow />}
    >
      {recommendedListings.map((currentListing: any, i) => (
        <Grow in={true} timeout={1000} key={currentListing.cardName + "_" + i}>
          <Paper>
            <ListingCard {...currentListing} />
          </Paper>
        </Grow>
      ))}
    </Carousel>
  );
}
