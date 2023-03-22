import * as React from 'react';
import { v4 as uuidv4 } from 'uuid';
import { Grid, Container, Typography, Grow, Paper } from '@mui/material';
import ListingCard from 'src/components/Listing/ListingCard';
import 'src/styles/ListingGrid.css';

export interface IListingGridProps {
  listings: Array<{
    listingId: string,
    cardName: string,
    author: string,
    price: string,
    image: string,
    alt: string,
    title: string,
    isbn: string,
    publisher: string,
    description: string,
    inventory: string,
    releaseDate: string
  }>
}

export default function ListingGrid({ listings }: IListingGridProps) {
  return (
    <>
      {listings.length === 0 &&
        <Typography variant="h4" align='center' color='text.secondary' mt={2}>
          No listings found
        </Typography>
      }
      {listings.length !== 0 &&
        <Container maxWidth="xl" className='grid'>
          <Grid container spacing={{ xs: 1, sm: 4 }} columns={{ xs: 1, sm: 8, md: 12 }}>
            {listings.map((currentListing) => {
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
