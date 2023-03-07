export const buildListings = (items :any) => {
    items = items.map((e :any) => {
        return {
            cardName: e.title,
            author: e.author,
            price: e.price,
            image: e.coverImage,
            alt: 'No image found'
        }
    })
    return items
}