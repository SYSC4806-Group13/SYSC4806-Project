import * as React from "react";
import { v4 as uuidv4 } from "uuid";
import {
  Grid,
  Container,
  Typography,
  Grow,
  Paper,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import ListingCard from "src/components/Listing/ListingCard";
import "src/styles/ListingGrid.css";

export interface IListingGridProps {
  listings: Array<{
    cardName: string;
    author: string;
    price: string;
    image: string;
    alt: string;
    title: string;
    listingId: string;
    isbn: string;
    publisher: string;
    description: string;
    inventory: string;
    releaseDate: string;
  }>;
}

const sortMenuOptions = [
  {
    sortingType: "Alphabetic",
    text: "Alphabetic (A-Z)",
  },
  {
    sortingType: "ReverseAlphabetic",
    text: "Reverse Alphabetic (Z-A)",
  },
  {
    sortingType: "Expensive",
    text: "Price (Most expensive)",
  },
  {
    sortingType: "Cheapest",
    text: "Price (Cheapest)",
  },
  {
    sortingType: "AuthorAlphabetic",
    text: "Author Alphabetic",
  },
  {
    sortingType: "ReleaseDateOldest",
    text: "Release Date (Oldest to Newest)",
  },
  {
    sortingType: "ReleaseDateNewest",
    text: "Release Date (Newest to Oldest)",
  },
];

export default function ListingGrid({ listings }: IListingGridProps) {
  const [sortingType, setSortingType] = React.useState("Alphabetic");
  const sortListings = (listings: Array<any>) => {
    const myClonedArray: any[] = [];
    listings.forEach((val) => myClonedArray.push(Object.assign({}, val)));
    switch (sortingType) {
      case "Alphabetic":
        myClonedArray.sort((a, b) => (a.title > b.title ? 1 : -1));
        break;
      case "ReverseAlphabetic":
        myClonedArray.sort((a, b) => (b.title > a.title ? 1 : -1));
        break;
      case "Expensive":
        myClonedArray.sort((a, b) => (a.price < b.price ? 1 : -1));
        break;
      case "Cheapest":
        myClonedArray.sort((a, b) => (b.price < a.price ? 1 : -1));
        break;
      case "AuthorAlphabetic":
        myClonedArray.sort((a, b) => (a.author > b.author ? 1 : -1));
        break;
      case "ReleaseDateOldest":
        myClonedArray.sort((a, b) => (a.releaseDate > b.releaseDate ? 1 : -1));
        break;
      case "ReleaseDateNewest":
        myClonedArray.sort((a, b) => (b.releaseDate > a.releaseDate ? 1 : -1));
        break;
    }
    return myClonedArray;
  };

  return (
    <>
      {listings.length === 0 && (
        <Typography variant="h4" align="center" color="text.secondary" mt={2}>
          No listings found
        </Typography>
      )}
      {listings.length !== 0 && (
        <Container maxWidth="xl" className="grid">
          <FormControl>
            <InputLabel variant="standard" htmlFor="uncontrolled-native">
              Listing Sorting
            </InputLabel>
            <Select
              defaultValue={"Alphabetic"}
              inputProps={{
                name: "listingSorting",
                id: "sortListings",
              }}
            >
              {sortMenuOptions.map((menuItem) => {
                return (
                  <MenuItem
                    value={menuItem.sortingType}
                    onClick={() => setSortingType(menuItem.sortingType)}
                  >
                    {menuItem.text}
                  </MenuItem>
                );
              })}
            </Select>
          </FormControl>
          <Grid
            container
            spacing={{ xs: 1, sm: 4 }}
            columns={{ xs: 1, sm: 8, md: 12 }}
          >
            {sortListings(listings).map((currentListing) => {
              return (
                <Grid item xs={1} sm={4} md={4} key={uuidv4()}>
                  <Grow in={true} timeout={1000}>
                    <Paper>
                      <ListingCard
                        { ...currentListing }
                      />
                    </Paper>
                  </Grow>
                </Grid>
              );
            })}
          </Grid>
        </Container>
      )}
    </>
  );
}
