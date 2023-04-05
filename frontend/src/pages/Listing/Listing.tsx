import React, { useContext, useEffect, useState } from "react";

import style from "./Listing.module.css";
import { UserLoginContext } from "src/context/userLoginContext";
import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Box,
  Button,
  CardMedia,
  Typography,
} from "@mui/material";
import PageHeader from "src/components/PageHeader/PageHeader";
import { useNavigate } from "react-router-dom";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import AddToCartButton from "src/components/Listing/AddToCartButton";
import { useHttpClient } from "src/hooks/http-hook";
import { API_BASE_URL, LISTING } from "src/constants/endpoints";
import RecommendedCarousel from "src/components/Recommended/RecommendedCarousel";
import { IListingCardProps } from "src/constants/common";

const Listing = () => {
  const { isLoggedIn } = useContext(UserLoginContext);
  const navigate = useNavigate();

  const [currentListing, setCurrentListing] = useState<IListingCardProps>();

  const listingId = window.location.href.split("/").at(-1) || "";

  const loginAndRedirectBackHere = () => {
    localStorage.setItem("amazin_redirectListing", listingId);
    navigate("/login");
  };

  const { sendRequest } = useHttpClient();

  useEffect(() => {
    const getSellerItems = async () => {
      let item: IListingCardProps = await sendRequest(
        LISTING,
        "GET",
        {},
        `/${listingId}`
      );
      setCurrentListing(item);
    };
    getSellerItems();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const [recommenedAccoridanExpanded, setRecommenedAccoridanExpanded] =
    React.useState(true);
  const [listingsAccoridanExpanded, setListingsAccoridanExpanded] =
    React.useState(true);

  const handleAccoridanToggle = (identifier: string) => () => {
    if (identifier === "recommended") {
      setRecommenedAccoridanExpanded(!recommenedAccoridanExpanded);
    } else if (identifier === "listings") {
      setListingsAccoridanExpanded(!listingsAccoridanExpanded);
    }
  };

  return (
    <>
      <PageHeader headerTitle={"Listing"}>
        <div className={style.back}>
          <Button onClick={() => navigate(-1)}>
            <ArrowBackIcon className={style.scale} fontSize="large" />
          </Button>
        </div>
        <div className={style.container}>
          <div className={style.img}>
            <Box>
              <CardMedia
                component="img"
                src={API_BASE_URL + "/covers/" + listingId}
                height="500"
                alt={currentListing && currentListing.alt}
                className="image"
              />
            </Box>
          </div>

          <div className={style.listingInfo}>
            <div className={style.listingTitle}>
              {currentListing && currentListing.title}
            </div>
            <div className={style.listingDescription}>
              <p>{currentListing && "Author : " + currentListing.author}</p>
              <p>
                {currentListing &&
                  "Description : " + currentListing.description}
              </p>
              <p>{currentListing && "ISBN : " + currentListing.isbn}</p>
              <p>{currentListing && "price : $" + currentListing.price}</p>
              <p>
                {currentListing && "stock left : " + currentListing.inventory}
              </p>
            </div>
            <div className={style.actions}>
              {!isLoggedIn ? (
                <Button
                  variant="contained"
                  color="success"
                  size="large"
                  data-testid="login button"
                  onClick={() => loginAndRedirectBackHere()}
                >
                  Login to add to cart
                </Button>
              ) : (
                <AddToCartButton listingId={listingId} />
              )}
            </div>
          </div>
        </div>
        <Accordion
          expanded={recommenedAccoridanExpanded}
          onChange={handleAccoridanToggle("recommended")}
        >
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel1a-content"
            id="panel1a-header"
          >
            <Typography gutterBottom variant="h3" align="center">
              Recommendations
            </Typography>
          </AccordionSummary>
          <AccordionDetails>
            <RecommendedCarousel />
          </AccordionDetails>
        </Accordion>
      </PageHeader>
    </>
  );
};

export default Listing;
