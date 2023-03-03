import * as React from 'react';
import { AppBar, Toolbar, Typography, Fab, IconButton } from '@mui/material';
import StorefrontIcon from '@mui/icons-material/Storefront';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import AccountCircle from '@mui/icons-material/AccountCircle';
import { Container } from '@mui/system';
import ScrollTop from './ScrollTop';

export interface IPageHeaderProps {
    headerTitle: string,
    children: React.ReactElement;
}

export default function PageHeader(props: IPageHeaderProps) {
    return (
        <React.Fragment>
            <AppBar>
                <Container>
                    <Toolbar>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                        >
                            <StorefrontIcon fontSize="large" sx={{ display: { md: 'flex' } }} />
                        </IconButton>
                        <Typography variant="h5" component="div" sx={{ flexGrow: 1 }}>
                            {props.headerTitle}
                        </Typography>
                        <IconButton
                            size="large"
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            color="inherit"
                        >
                            <AccountCircle />
                        </IconButton>
                    </Toolbar>
                </Container>
            </AppBar>
            <Toolbar id="back-to-top-anchor" />
            <Container>
                {props.children}
            </Container>
            <ScrollTop {...props}>
                <Fab color="primary" size="large" aria-label="scroll back to top">
                    <KeyboardArrowUpIcon />
                </Fab>
            </ScrollTop>
        </React.Fragment>
    );
}
