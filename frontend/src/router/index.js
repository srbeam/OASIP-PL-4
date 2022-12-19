import { createRouter, createWebHistory } from 'vue-router'
import ListAllEvent from '../views/ListAllEvent.vue'
import EventDetail from '../views/EventDetail.vue'
import AddEvent from '../views/AddEvent.vue'
import ListCategory from '../views/ListCategory.vue'
import ListByCategory from '../views/ListByCategory.vue'
import Home from '../views/Home.vue'
import ListByDate from '../views/ListByDate.vue'
import AboutUs from '../views/AboutUs.vue'
import ListUser from '../views/ListUser.vue'

const history = createWebHistory('/pl4/')
const routes = [
	{ path: '/', name: 'Home', component: Home },
	{ path: '/events', name: 'ListAll', component: ListAllEvent },
	{ path: '/events/:id', name: 'EventDetail', component: EventDetail },
	{ path: '/events/add', name: 'AddEvent', component: AddEvent },
	{ path: '/categories', name: 'Category', component: ListCategory },
	{ path: '/categories/:id', name: 'ListByCategory', component: ListByCategory },
	{ path: '/date', name: 'ListByDate', component: ListByDate },
	{ path: '/about-us', name: 'AboutUs', component: AboutUs },
	{ path: '/listalluser', name: 'ListUser', component: ListUser }
]

const router = createRouter({ history, routes })
router.beforeEach((to, from, next) => {
	if (!localStorage.getItem('token')) {
		if (
			to.path === '/' ||
			to.path === '/events/add' ||
			to.path === '/categories' ||
			to.path === '/about-us'
		) {
			next()
		} else {
			next({ name: 'Home' })
		}
	} else {
		if (localStorage.getItem('role') == 'ROLE_student') {
			if (to.path === '/listalluser') {
				next('/events/add')
			} else {
				next()
			}
		} else if (localStorage.getItem('role') == 'ROLE_lecturer') {
			if (to.path === '/events/add' || to.path === '/listalluser') {
				next('/events')
			} else {
				next()
			}
		} else {
			next()
		}
	}
})

export default router
