package pl.com.devmeet.devmeetcore.user.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
class UserApi {

    private UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    // get

    @GetMapping
    public List<UserDto> getFiltered(@RequestParam(required = false) String searchEmalOrPhone) {
        if (searchEmalOrPhone != null)
            return userService.findAllByEmailAndPhone(searchEmalOrPhone);
        else return userService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/is-active/{isActive}")
    public List<UserDto> getAllIsActive(@PathVariable String isActive) {
        return userService.findAllByIsActive(Boolean.valueOf(isActive));
    }


    // add

    @PostMapping
    public ResponseEntity<UserDto> add(@RequestBody UserDto user) {
        UserDto added = userService.add(user);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(added.getId())
                .toUri();
        return ResponseEntity.created(uri).body(added);
    }

    // update

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
                                          @RequestBody UserDto user) {
        if (!id.equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id from path does not match with id in body!");

        if (userService.update(user) != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity removeUserById(@PathVariable Long id) {
        return (userService.delete(id))
                ? new ResponseEntity(HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
