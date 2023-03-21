export const buildListings = (items :any) => {
    items = items.map((e :any) => {
        return {
            cardName: e.title,
            author: e.author,
            listingId: e.listingId,
            price: e.price,
            alt: 'No image found'
        }
    })
    return items
}