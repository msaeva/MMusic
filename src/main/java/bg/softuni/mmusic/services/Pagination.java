package bg.softuni.mmusic.services;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class Pagination<T extends Collection> {
    public Pagination(Integer count, Integer offset, Long totalRecords) {
        this.count = count;
        this.offset = offset;
        this.totalRecords = totalRecords;
    }

    public Integer count;
    public Integer offset;
    public Long totalRecords;

    public T data;
}
