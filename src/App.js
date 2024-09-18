import { useState, useEffect } from "react";

// react-router components
import { Routes, Route, Navigate, useLocation } from "react-router-dom";

// @mui material components
import { ThemeProvider } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import Icon from "@mui/material/Icon";
import MDBox from "components/MDBox";
import Sidenav from "examples/Sidenav";
import Configurator from "examples/Configurator";
import theme from "assets/theme";
import routes from "routes";
import { useMaterialUIController, setOpenConfigurator } from "context";
import brandWhite from "assets/images/logo-ct.png";

import { Account } from "Account";
import Status  from "Status";

import AuthenticatedRoute from "AuthenticatedRoute";

export default function App() {
  const [controller, dispatch] = useMaterialUIController();
  const { layout, openConfigurator } = controller;
  const { pathname } = useLocation();

  // Setting page scroll to 0 when changing the route
  useEffect(() => {
    document.documentElement.scrollTop = 0;
    document.scrollingElement.scrollTop = 0;
  }, [pathname]);

  const getUnprotectedRoutes = (allRoutes) =>
  allRoutes.map((route) => {
    if (route.collapse) {
      return getRoutes(route.collapse);
    }

    if (route.route && !route.isProtected) {
      return <Route path={route.route} element={route.component} key={route.key} />;
    }

    return null;
  });

  const getRoutes = (allRoutes) =>
    allRoutes.map((route) => {
      if (route.collapse) {
        return getRoutes(route.collapse);
      }

      if (route.route && route.isProtected) {
        return <Route path={route.route} element={route.component} key={route.key} />;
      }

      return null;
    });

  const configsButton = (
    <MDBox
      display="flex"
      justifyContent="center"
      alignItems="center"
      width="3.25rem"
      height="3.25rem"
      bgColor="white"
      shadow="sm"
      borderRadius="50%"
      position="fixed"
      right="2rem"
      bottom="2rem"
      zIndex={99}
      color="dark"
      sx={{ cursor: "pointer" }}
      onClick={() => setOpenConfigurator(dispatch, !openConfigurator)}
    >
      <Icon fontSize="small" color="inherit">
        settings
      </Icon>
    </MDBox>
  );

  const visibleRoutes = routes.filter((route) => route.visible);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Account>
        {/* <Status/> */}
        {layout === "dashboard" && (
          <>
            <Sidenav brand={brandWhite} brandName="Admin Panel" routes={visibleRoutes} />
            <Configurator />
          </>
        )}
        <Routes>
            {getUnprotectedRoutes(routes)}
        <Route element={<AuthenticatedRoute />}>
            {getRoutes(routes)}
        </Route>
        <Route path="*" element={<Navigate to="/users" replace />} />
      </Routes>
      </Account>
    </ThemeProvider>
  );
}
