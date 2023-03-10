import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import {
  AppBar,
  Toolbar,
  Typography,
  Fab,
  IconButton,
  Menu,
  MenuItem,
} from "@mui/material";
import StorefrontIcon from "@mui/icons-material/Storefront";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import AccountCircle from "@mui/icons-material/AccountCircle";
import { Container } from "@mui/system";
import ScrollTop from "src/components/ScrollTop";
import { UserLoginContext } from "src/context/userLoginContext";
import { GOOGLE_AUTH_URL } from "src/constants/endpoints";

export interface IPageHeaderProps {
  headerTitle: string;
  children: React.ReactElement | React.ReactElement[];
}

export default function PageHeader(props: IPageHeaderProps) {
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const navigate = useNavigate();
  const { isLoggedIn, logOut } = useContext(UserLoginContext);

  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleMyAccount = () => {
    navigate("/seller/1");
    handleClose();
  };

  const handleHome = () => {
    navigate("/");
    handleClose();
  };

  const handleLogout = (e: React.MouseEvent<HTMLElement>) => {
    e.preventDefault();
    logOut();
    handleClose();
  };

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
              <StorefrontIcon
                fontSize="large"
                sx={{ display: { md: "flex" } }}
              />
            </IconButton>
            <Typography variant="h5" component="div" sx={{ flexGrow: 1 }}>
              {props.headerTitle}
            </Typography>
            {/* TODO - Make this Log in button go to a page*/}
            {!isLoggedIn && (
              <a
                href={GOOGLE_AUTH_URL}
                rel="noopener noreferrer"
                className="button"
                data-testid="login button"
              >
                Log In
              </a>
            )}
            {isLoggedIn && (
              <>
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
                    vertical: "top",
                    horizontal: "right",
                  }}
                  keepMounted
                  transformOrigin={{
                    vertical: "top",
                    horizontal: "right",
                  }}
                  open={Boolean(anchorEl)}
                  onClose={handleClose}
                >
                  <MenuItem onClick={handleMyAccount}>My Account</MenuItem>
                  <MenuItem onClick={handleLogout}>Log Out</MenuItem>
                </Menu>
              </>
            )}
          </Toolbar>
        </Container>
      </AppBar>
      <Toolbar id="back-to-top-anchor" />
      <Container>{props.children}</Container>
      <ScrollTop {...props}>
        <Fab color="primary" size="large" aria-label="scroll back to top">
          <KeyboardArrowUpIcon />
        </Fab>
      </ScrollTop>
    </React.Fragment>
  );
}
