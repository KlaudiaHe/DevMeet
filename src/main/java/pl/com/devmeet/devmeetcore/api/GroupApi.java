package pl.com.devmeet.devmeetcore.api;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudService;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/groups")
class GroupApi {

    private GroupCrudService group;
    private MemberCrudService member;

    @Autowired
    private GroupApi(GroupCrudService group, MemberCrudService member) {
        this.group = group;
        this.member = member;
    }

    @GetMapping
    private ResponseEntity<List<GroupDto>> getAllOrFiltered(@RequestParam(required = false) String searchText) {
        return new ResponseEntity<>(group.findBySearchText(searchText), HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("{id}")
    private ResponseEntity<GroupDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(group.findById(id), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping
    private ResponseEntity<GroupDto> add(@RequestBody GroupDto newGroup) {
        this.group.add(newGroup);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newGroup.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newGroup);
    }

    @SneakyThrows
    @PutMapping
    private ResponseEntity<GroupDto> update(@RequestBody GroupDto updatedGroup) {
//        if (!id.equals(updatedGroup.getId()))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id from path does not match with id in body!");
        group.update(updatedGroup);
        return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    private HttpStatus deactivateById(@PathVariable Long id) {
        group.delete(group.findById(id));
        return HttpStatus.OK;
    }
}
