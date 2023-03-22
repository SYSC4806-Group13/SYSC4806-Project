export const buildListings = (items: any) => {
    items = items.map((e: any) => {
        return {
            listingId: e.listingId,
            cardName: e.title,
            author: e.author,
            price: e.price,
            image: e.coverImage,
            isbn: e.isbn,
            publisher: e.publisher,
            description: e.description,
            inventory: e.inventory,
            releaseDate: e.releaseDate,
            title: e.title,
            alt: 'No image found'
        }
    })
    return items
}