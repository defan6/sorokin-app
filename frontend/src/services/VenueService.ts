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

    getVenues() {
        return api.get<Venue[]>(API_URL);
    }

    createVenue(data: CreateVenueData) {
        return api.post<Venue>(API_URL, data);
    }

    deleteVenue(id: number) {
        return api.delete(`${API_URL}/${id}`);
    }

    getVenueById(id: number) {
        return api.get<Venue>(`${API_URL}/${id}`);
    }


    updateVenue(id: number, data: Partial<CreateVenueData>) {
        return api.patch<Venue>(`${API_URL}/${id}`, data);
    }
}


export default new VenueService();
