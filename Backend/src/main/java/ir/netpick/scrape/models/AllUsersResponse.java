package ir.netpick.scrape.models;

import java.util.List;

public record AllUsersResponse(
        List<UserDTO> users,
        Integer totalPageCount,
        Integer currentPage) {

}
