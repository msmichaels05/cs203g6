/**
  For adding a new route you can follow the existing routes in the routes array.
  1. The `type` key with the `collapse` value is used for a route.
  2. The `type` key with the `title` value is used for a title inside the Sidenav. 
  3. The `type` key with the `divider` value is used for a divider between Sidenav items.
  4. The `name` key is used for the name of the route on the Sidenav.
  5. The `key` key is used for the key of the route (It will help you with the key prop inside a loop).
  6. The `icon` key is used for the icon of the route on the Sidenav, you have to add a node.
  7. The `collapse` key is used for making a collapsible item on the Sidenav that has other routes
  inside (nested routes), you need to pass the nested routes inside an array as a value for the `collapse` key.
  8. The `route` key is used to store the route location which is used for the react router.
  9. The `href` key is used to store the external links location.
  10. The `title` key is only for the item with the type of `title` and its used for the title text on the Sidenav.
  10. The `component` key is used to store the component of its route.
*/

// Material Dashboard 2 React layouts
// import Dashboard from "layouts/dashboard";
import Users from "layouts/users";
import Notifications from "layouts/notifications";
import SignIn from "layouts/authentication/sign-in";
import SignUp from "layouts/authentication/sign-up";
import EditUser from "layouts/users/EditUser.js";
import EnrollUser from "layouts/users/EnrollUser.js";
import DeleteUser from "layouts/users/DeleteUser.js";
import AdjustPoints from "layouts/users/AdjustPoints";
import DetailsLog from "layouts/logs/DetailsLog.js";
import Logs from "layouts/logs";
import SignOut from "layouts/authentication/sign-out";
import Icon from "@mui/material/Icon";
import Redirect from "layouts/redirect";


const routes = [
  // {
  //   type: "collapse",
  //   name: "Dashboard",
  //   key: "dashboard",
  //   icon: <Icon fontSize="small">dashboard</Icon>,
  //   route: "/dashboard",
  //   component: <Dashboard />,
  //   visible: false,
  // },
  {
    type: "collapse",
    name: "Users",
    key: "users",
    icon: <Icon fontSize="small">person</Icon>,
    route: "/users",
    component: <Users />,
    visible: true,
    isProtected: true,
  },
  {
    type: "collapse",
    name: "Logs",
    key: "logs",
    icon: <Icon fontSize="small">table_view</Icon>,
    route: "/logs",
    component: <Logs />,
    visible: true,
    isProtected: true,
  },
  // {
  //   type: "collapse",
  //   name: "Tables",
  //   key: "tables",
  //   icon: <Icon fontSize="small">table_view</Icon>,
  //   route: "/tables",
  //   component: <Tables />,
  //   visible: false,
  // },
  // {
  //   type: "collapse",
  //   name: "Billing",
  //   key: "billing",
  //   icon: <Icon fontSize="small">receipt_long</Icon>,
  //   route: "/billing",
  //   component: <Billing />,
  //   visible: false,
  // },
  {
    type: "collapse",
    name: "Notifications",
    key: "notifications",
    icon: <Icon fontSize="small">notifications</Icon>,
    route: "/notifications",
    component: <Notifications />,
    visible: true,
    isProtected: true,
  },
  // {
  //   type: "collapse",
  //   name: "Profile",
  //   key: "profile",
  //   icon: <Icon fontSize="small">person</Icon>,
  //   route: "/profile",
  //   component: <Profile />,
  //   visible: false,
  // },

  {
    type: "collapse",
    name: "Sign In",
    key: "sign-in",
    icon: <Icon fontSize="small">login</Icon>,
    route: "/authentication/sign-in",
    component: <SignIn />,
    visible: false,
    isProtected: false,
  },
  {
    type: "collapse",
    name: "Sign Out",
    key: "sign-out",
    icon: <Icon fontSize="small">login</Icon>,
    route: "/authentication/sign-out",
    component: <SignOut />,
    visible: true,
    isProtected: false,
  },
  {
    type: "collapse",
    name: "Sign Up",
    key: "sign-up",
    icon: <Icon fontSize="small">assignment</Icon>,
    route: "/authentication/sign-up",
    component: <SignUp />,
    visible: false,
    isProtected: false,
  },
  {
    type: "collapse",
    name: "Edit User",
    key: "edit-user",
    icon: <Icon fontSize="small">edit</Icon>,
    route: "/edit-user/:id",
    component: <EditUser />,
    visible: false,
    isProtected: true,
  },
  {
    type: "collapse",
    name: "Enroll User",
    key: "enroll-user",
    icon: <Icon fontSize="small"></Icon>,
    route: "/enroll-user",
    component: <EnrollUser />,
    visible: false,
    isProtected: true,
  },
  {
    type: "collapse",
    name: "Delete User",
    key: "delete-user",
    icon: <Icon fontSize="small"></Icon>,
    route: "/delete-user/:id",
    component: <DeleteUser />,
    visible: false,
    isProtected: true,
  },
  {
    type: "collapse",
    name: "Adjust Points",
    key: "adjust-points",
    icon: <Icon fontSize="small"></Icon>,
    route: "/adjust-points/:id",
    component: <AdjustPoints />,
    visible: false,
    isProtected: true,
  },
  {
    type: "collapse",
    name: "Log Details",
    key: "log-details",
    icon: <Icon fontSize="small"></Icon>,
    route: "/log-details/:logid",
    component: <DetailsLog />,
    visible: false,
    isProtected: true,
  },
  {
    type: "collapse",
    name: "Redirect",
    key: "log-details",
    icon: <Icon fontSize="small"></Icon>,
    route: "/redirect",
    component: <Redirect />,
    visible: false,
    isProtected: true,
  },
];

export default routes;
