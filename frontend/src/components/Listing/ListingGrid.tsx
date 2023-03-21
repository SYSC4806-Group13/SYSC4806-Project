import * as React from 'react';
import { v4 as uuidv4 } from 'uuid';
import { Grid, Container, Typography, Grow, Paper } from '@mui/material';
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
    if (props.listings.length === 0) {
        return (
            <Typography variant="h4" align='center' color='text.secondary' mt={2}>
                No listings found
            </Typography>
        );
    }
    return (
        <Container maxWidth="xl" className='grid'>
            <Grid container spacing={{ xs: 1, sm: 4 }} columns={{ xs: 1, sm: 8, md: 12 }}>
                {props.listings.map((x) => {
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
