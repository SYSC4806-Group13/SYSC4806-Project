import * as React from 'react';
import { v4 as uuidv4 } from 'uuid';
import {Grid, Container, Typography, Grow, Paper, FormControl, InputLabel, Select, MenuItem} from '@mui/material';
import ListingCard from './ListingCard';
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

export default function ListingGrid(props: IListingGridProps) {
    const [sortingType,setSortingType] = React.useState("A-Z");
    if (props.listings.length === 0) {
        return (
            <Typography variant="h4" align='center' color='text.secondary' mt={2}>
                No listings found
            </Typography>
        );
    }

    const sortListings = (listings : Array<any>) => {
        console.log("I was called");
        const myClonedArray: any[] = [];
        listings.forEach(val => myClonedArray.push(Object.assign({}, val)));
        switch (sortingType){
            case "A-Z":
                myClonedArray.sort((a,b)=>(a.title>b.title)? 1: -1);
                break;
            case "Z-A":
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
        <Container maxWidth="xl" className='grid'>
            <FormControl>
                <InputLabel variant="standard" htmlFor="uncontrolled-native">
                    Listing Sorting
                </InputLabel>
                <Select
                    defaultValue={"A-Z"}
                    inputProps={{
                        name: 'listingSorting',
                        id: 'sortListings',
                    }}
                >
                    <MenuItem value={"A-Z"} onClick={() => setSortingType("A-Z")}>Alphabetic (A-Z)</MenuItem>
                    <MenuItem value={"Z-A"} onClick={() => setSortingType("Z-A")}>Reverse Alphabetic (Z-A)</MenuItem>
                    <MenuItem value={"Expensive"} onClick={() => setSortingType("Expensive")}>Price (Most expensive)</MenuItem>
                    <MenuItem value={"Cheapest"} onClick={() => setSortingType("Cheapest")}>Price (Cheapest)</MenuItem>
                    <MenuItem value={"AuthorAlphabetic"}onClick={() => setSortingType("AuthorAlphabetic")}>Author Alphabetic</MenuItem>
                </Select>
            </FormControl>
            <Grid container spacing={{ xs: 1, sm: 4 }} columns={{ xs: 1, sm: 8, md: 12 }}>
                {sortListings(props.listings).map((x) => {
                    return (
                        <Grid item xs={1} sm={4} md={4} key={uuidv4()}>
                            <Grow in={true} timeout={1000}>
                                <Paper>
                                    <ListingCard cardName={x.cardName} author={x.author} price={x.price} image={x.image} alt={x.alt}
                                                 description={x.description} inventory={x.inventory}
                                                 listingId={x.listingId} isbn={x.isbn}
                                                 publisher={x.publisher} releaseDate={x.releaseDate} title={x.title}/>
                                </Paper>
                            </Grow>
                        </Grid>
                    )
                })}
            </Grid>
        </Container>

    );
}
