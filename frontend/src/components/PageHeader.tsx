import * as React from 'react';
import { useNavigate } from "react-router-dom";
import { AppBar, Toolbar, Typography, Fab, IconButton, Menu, MenuItem } from '@mui/material';
import StorefrontIcon from '@mui/icons-material/Storefront';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import AccountCircle from '@mui/icons-material/AccountCircle';
import { Container } from '@mui/system';
import ScrollTop from 'src/components/ScrollTop';

export interface IPageHeaderProps {
    headerTitle: string,
    children: React.ReactElement | React.ReactElement[];
}

export default function PageHeader(props: IPageHeaderProps) {
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const navigate = useNavigate();

    const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget)
    };

    const handleClose = () => {
        setAnchorEl(null)
    };

    const handleMyAccount = () => {
        navigate("/seller/1")
        handleClose()
    }

    const handleHome = () => {
        navigate("/")
        handleClose()
    }
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
                            onClick={handleHome}
                        >
                            <StorefrontIcon fontSize="large" sx={{ display: { md: 'flex' } }}/>
                        </IconButton>
                        <Typography variant="h5" component="div" sx={{ flexGrow: 1 }}>
                            {props.headerTitle}
                        </Typography>
                        <IconButton
                            size="large"
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            onClick={handleMenu}
                            color="inherit"
                        >
                            <AccountCircle />
                        </IconButton>
                        <Menu
                            id="menu-appbar"
                            anchorEl={anchorEl}
                            anchorOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            open={Boolean(anchorEl)}
                            onClose={handleClose}
                        >
                            <MenuItem onClick={handleMyAccount}>My Account</MenuItem>
                        </Menu>
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