import React from 'react'
const Dashboard = React.lazy(() => import('./views/dashboard/Dashboard'))
const AddStation = React.lazy(() => import('./views/pages/station/AddStation'))
const Stations = React.lazy(() => import('./views/pages/station/Stations'))
const AddRoute = React.lazy(() => import('./views/pages/route/AddRoute'))
const Routes = React.lazy(() => import('./views/pages/route/Routes'))


const Bikes = React.lazy(() => import('./views/pages/buses/Buses'))
const EditBike = React.lazy(() => import('./views/pages/editBus/EditBus'))
const AddBike = React.lazy(() => import('./views/pages/addBus/AddBus'))

const Payments = React.lazy(() => import('./views/pages/payments/Payments'))
const ViewBooking = React.lazy(() => import('./views/pages/viewBooking/ViewBooking'))
const Contact = React.lazy(() => import('./views/pages/contact/Contact'))



const routes = [
  { path: '/', exact: true, name: 'Home' },
  { path: '/dashboard', name: 'Dashboard', element: Dashboard },

  { path: '/pages/station', name: 'Station', element: AddStation },
  { path: '/pages/stations', name: 'Stations', element: Stations },

  { path: '/pages/addRoute', name: 'AddRoute', element: AddRoute },
  { path: '/pages/routes', name: 'Routes', element: Routes },



  { path: '/pages/Bikes', name: 'Bikes', element: Bikes },
  { path: '/pages/editBike', name: 'EditBike', element: EditBike },
  { path: '/pages/addBike', name: 'AddBike', element: AddBike },

  { path: '/pages/payments', name: 'Payments', element: Payments },
  { path: '/pages/viewBooking', name: 'ViewBooking', element: ViewBooking },
  { path: '/pages/contact', name: 'Contact', element: Contact },
]

export default routes
