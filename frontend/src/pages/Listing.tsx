import * as React from 'react';
import ListingGrid from '../components/ListingGrid';
import PageHeader from '../components/PageHeader';

export interface IListingProps {
}

export default function Listing(props: IListingProps) {
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
