import * as React from 'react';
import { v4 as uuidv4 } from 'uuid';
import {Grid, Container, Typography, Grow, Paper, FormControl, InputLabel, Select, MenuItem} from '@mui/material';
import ListingCard from 'src/components/Listing/ListingCard';
import 'src/styles/ListingGrid.css'

export interface IListingGridProps {
    listings: Array<{
        cardName: string,
        author: string,
        price: string,
        image: string,
        alt: string,
        title: string,
        listingId : string,
        isbn: string,
        publisher: string,
        description: string,
        inventory: string,
        releaseDate: string
    }>
}

export default function ListingGrid({ listings }: IListingGridProps) {
    const [sortingType,setSortingType] = React.useState("Alphabetic");
    const sortListings = (listings : Array<any>) => {
        const myClonedArray: any[] = [];
        listings.forEach(val => myClonedArray.push(Object.assign({}, val)));
        switch (sortingType){
            case "Alphabetic":
                myClonedArray.sort((a,b)=>(a.title>b.title)? 1: -1);
                break;
            case "ReverseAlphabetic":
                myClonedArray.sort((a,b)=>(b.title>a.title)? 1: -1);
                break;
            case "Expensive":
                myClonedArray.sort((a,b)=>(a.price<b.price)? 1: -1);
                break;
            case "Cheapest":
                myClonedArray.sort((a,b)=>(b.price<a.price)? 1: -1);
                break;
            case "AuthorAlphabetic":
                myClonedArray.sort((a,b)=>(a.author>b.author)? 1: -1);
                break;
        }
        return myClonedArray;
    }

    return (
    <>
      {listings.length === 0 &&
        <Typography variant="h4" align='center' color='text.secondary' mt={2}>
          No listings found
        </Typography>
      }
      {listings.length !== 0 &&
        <Container maxWidth="xl" className='grid'>
            <FormControl>
                <InputLabel variant="standard" htmlFor="uncontrolled-native">
                    Listing Sorting
                </InputLabel>
                <Select
                    defaultValue={"Alphabetic"}
                    inputProps={{
                        name: 'listingSorting',
                        id: 'sortListings',
                    }}
                >
                    <MenuItem value={"Alphabetic"} onClick={() => setSortingType("Alphabetic")}>Alphabetic (A-Z)</MenuItem>
                    <MenuItem value={"ReverseAlphabetic"} onClick={() => setSortingType("ReverseAlphabetic")}>Reverse Alphabetic (Z-A)</MenuItem>
                    <MenuItem value={"Expensive"} onClick={() => setSortingType("Expensive")}>Price (Most expensive)</MenuItem>
                    <MenuItem value={"Cheapest"} onClick={() => setSortingType("Cheapest")}>Price (Cheapest)</MenuItem>
                    <MenuItem value={"AuthorAlphabetic"}onClick={() => setSortingType("AuthorAlphabetic")}>Author Alphabetic</MenuItem>
                </Select>
            </FormControl>
          <Grid container spacing={{ xs: 1, sm: 4 }} columns={{ xs: 1, sm: 8, md: 12 }}>
            {sortListings(listings).map((currentListing) => {
              return (
                <Grid item xs={1} sm={4} md={4} key={uuidv4()}>
                  <Grow in={true} timeout={1000}>
                    <Paper>
                      <ListingCard
                        cardName={currentListing.cardName}
                        author={currentListing.author}
                        price={currentListing.price}
                        image={currentListing.image}
                        alt={currentListing.alt}
                        description={currentListing.description}
                        inventory={currentListing.inventory}
                        listingId={currentListing.listingId}
                        isbn={currentListing.isbn}
                        publisher={currentListing.publisher}
                        releaseDate={currentListing.releaseDate}
                        title={currentListing.title}
                      />
                    </Paper>
                  </Grow>
                </Grid>
              )
            })}
          </Grid>
        </Container>
      }
    </>
  );
}
