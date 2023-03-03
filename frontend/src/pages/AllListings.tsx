import * as React from 'react';
import ListingGrid from 'src/components/Listing/ListingGrid';
import PageHeader from 'src/components/PageHeader';

export interface IAllListingsProps {
}

export default function AllListings(props: IAllListingsProps) {
    const listing = [
        {
            cardName: 'test',
            author: 'test',
            price: '12.22',
            image: '/static/images/lizard.jpg',
            alt: 'lizard'
        },
        {
            cardName: 'test',
            author: 'test',
            price: '12.22',
            image: '/static/images/lizard.jpg',
            alt: 'lizard'
        },
        {
            cardName: 'test',
            author: 'test',
            price: '12.22',
            image: '/static/images/book-cover.jpg',
            alt: 'book'
        },
        {
            cardName: 'test',
            author: 'test',
            price: '12.22',
            image: '/static/images/book-cover.jpg',
            alt: 'book'
        },
        {
            cardName: 'test',
            author: 'test',
            price: '12.22',
            image: '/static/images/book-cover.jpg',
            alt: 'book'
        }
    ]
    return (
        <React.Fragment>
            <PageHeader headerTitle='Listing'>
                <ListingGrid listings={listing} />
            </PageHeader>
        </React.Fragment>
    );
}
