export const buildListings = (items: any) => {
  items = items.map((e: any) => {
    return {
      ...e,
      cardName: e.title,
      image: e.coverImage,
      alt: "No image found",
    };
  });
  return items;
};
