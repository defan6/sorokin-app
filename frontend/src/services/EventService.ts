import api from './api';

const API_URL = '/api/manager/events';

export interface Event {
  id: number;
  title: string;
  description: string;
  eventDate: string;
  venueId: number
  // Добавьте другие поля, если они есть
}

type CreateEventData = Omit<Event, 'id'>;

class EventService {
  getEvents() {
    return api.get<Event[]>(API_URL);
  }

  getEventById(id: number) {
    return api.get<Event>(`${API_URL}/${id}`);
  }

  updateEvent(id: number, data: Partial<CreateEventData>) {
    return api.patch<Event>(`${API_URL}/admin/${id}`, data);
  }

  deleteEvent(id: number) {
    return api.delete(`${API_URL}/admin/${id}`);
  }

  createEvent(data: CreateEventData){
      return api.post<Event>(API_URL + '/admin', data);
    }
}

const eventService = new EventService();
export default eventService;
