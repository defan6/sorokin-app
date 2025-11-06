import api from './api';

const API_URL = '/api/manager/venues/admin';

export interface Venue {
    id: number;
    name: string;
    address: string;
    capacity: number;
}


type CreateVenueData = Omit<Venue, 'id'>;


class VenueService {

    getVenues(){
        return api.get<Venue[]>(API_URL);
    }

    createVenue(data: CreateVenueData){
        return api.post<Venue>(API_URL, data);
    }
}


export default new VenueService();
