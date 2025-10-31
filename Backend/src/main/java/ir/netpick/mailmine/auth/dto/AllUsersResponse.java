package ir.netpick.mailmine.auth.dto;

import java.util.List;

public record AllUsersResponse(
                List<UserDTO> users,
                Integer totalPageCount,
                Integer currentPage) {

}
