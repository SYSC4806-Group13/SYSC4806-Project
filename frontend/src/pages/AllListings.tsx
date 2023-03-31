import * as React from "react";
import { Typography, Accordion, AccordionSummary, AccordionDetails } from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ListingGrid from "src/components/Listing/ListingGrid";
import PageHeader from "src/components/PageHeader/PageHeader";
import { LISTING } from "src/constants/endpoints";
import { useHttpClient } from "src/hooks/http-hook";
import { buildListings } from "src/utils/listings";
import RecommendedCarousel from 'src/components/Recommended/RecommendedCarousel'

export interface IAllListingsProps { }

export default function AllListings(props: IAllListingsProps) {
    const [listings, setListings] = React.useState([]);
    const { sendRequest } = useHttpClient();
    React.useEffect(() => {
        const getSellerItems = async () => {
            let items = await sendRequest(LISTING, "GET", {});
            items = buildListings(items);
            setListings(items);
        };
        getSellerItems();
    }, [sendRequest]);

    const [recommenedAccoridanExpanded, setRecommenedAccoridanExpanded] = React.useState(true)
    const [listingsAccoridanExpanded, setListingsAccoridanExpanded] = React.useState(true)

    const handleAccoridanToggle = (identifier: string) => () => {
        if (identifier === 'recommended') {
            setRecommenedAccoridanExpanded(!recommenedAccoridanExpanded)
        } else if (identifier === 'listings') {
            setListingsAccoridanExpanded(!listingsAccoridanExpanded)
        }
      };
    return (
        <React.Fragment>
            <PageHeader headerTitle='Listing'>
                <Accordion expanded={recommenedAccoridanExpanded} onChange={handleAccoridanToggle('recommended')}>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <Typography gutterBottom variant="h3" align='center'>
                            Recommendations
                        </Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                        <RecommendedCarousel />
                    </AccordionDetails>
                </Accordion>
                <Accordion expanded={listingsAccoridanExpanded} onChange={handleAccoridanToggle('listings')}>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <Typography gutterBottom variant="h3" align='center'>
                            Listings
                        </Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                        <ListingGrid listings={listings} />
                    </AccordionDetails>
                </Accordion>
            </PageHeader>
        </React.Fragment>
    );
}
