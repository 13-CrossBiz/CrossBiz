package mutsa.backend.Visa.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mutsa.backend.Visa.dto.response.NearPlaceDto;
import mutsa.backend.Visa.repository.VisaPlaceRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisaPlaceService {
    private final VisaPlaceRepository repo;
    private static final double EARTH_RADIUS_M = 6371_000.0;
    public static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(rLat1) * Math.cos(rLat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS_M * c;
    }

    public List<NearPlaceDto> calculateDistance(double lat, double lon) {
        return repo.findAll().stream()
                .filter(bd -> bd.getLatitude() != null && bd.getLongitude() != null)
                .map(bd -> NearPlaceDto.builder()
                        .id(bd.getVisaplaceId())
                        .name(bd.getName())
                        .address(bd.getAddress())
                        .latitude(bd.getLatitude())
                        .longitude(bd.getLongitude())
                        .phoneNumber(bd.getPhoneNumber())
                        .distanceMeters(
                                haversineMeters(lat, lon, bd.getLatitude(), bd.getLongitude())
                        )
                        .build()
                )
                .sorted(Comparator.comparingDouble(NearPlaceDto::getDistanceMeters))
                .limit(3)
                .toList();
    }
}
