import api from './api';

const API_URL = '/api/manager/events';

export interface Event {
  id: number;
  name: string;
  description: string;
  venueId: number
  // Добавьте другие поля, если они есть
}

type CreateEventData = Omit<Event, 'id'>;

class EventService {
  getEvents() {
    return api.get<Event[]>(API_URL);
  }

  // Здесь можно будет добавить методы для создания, обновления, удаления событий
    getEventById(id: string) {
      return api.get<Event>(`${API_URL}/${id}`)
    }


    createEvent(data: CreateEventData){
      return api.post<Event>(API_URL, data);
    }
}

export default new EventService();
