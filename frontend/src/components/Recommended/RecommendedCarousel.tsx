import React from 'react';
import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import { Grow, Paper } from '@mui/material';
import ListingCard from 'src/components/Listing/ListingCard';
import 'src/styles/Carousel.css'

export interface IRecommendedCarouselProps {
    listings: Array<{
        cardName: string,
        author: string,
        price: string,
        image: string,
        alt: string
    }>
}

export default function RecommendedCarousel(props: IRecommendedCarouselProps) {
    const responsive = {
        superLargeDesktop: {
            // the naming can be any, depends on you.
            breakpoint: { max: 4000, min: 3000 },
            items: 5
        },
        desktop: {
            breakpoint: { max: 3000, min: 1024 },
            items: 3
        },
        tablet: {
            breakpoint: { max: 1024, min: 464 },
            items: 2
        },
        mobile: {
            breakpoint: { max: 464, min: 0 },
            items: 1
        }
    };
    return (
        <Carousel
            responsive={responsive}
            draggable={false}
            autoPlay={true}
            containerClass="carousel"
            itemClass="carousel-item"
            customRightArrow={<ArrowForwardIosIcon  fontSize='large' className='arrow right' />}
            customLeftArrow={<ArrowBackIosNewIcon fontSize='large' className='arrow left'/>}
        >
            {props.listings.map(x => (
                <Grow in={true} timeout={1000} key={x.cardName}>
                    <Paper>
                        <ListingCard carousel cardName={x.cardName} author={x.author} price={x.price} image={x.image} alt={x.alt} />
                    </Paper>
                </Grow>
            ))}
        </Carousel>
    );
}