package com.shopme.admin.category.util;
import com.shopme.admin.category.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {

    public static final int ROOT_CATEGORIES_PER_PAGE = 4;

    private final CategoryRepository categoryRepository;
    private final Sort sortByName = Sort.by("name").ascending();

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public Iterable<Category> listByPage(
            CategoryPageInfor categoryInfor,
            int pageNumber, String sortDir, String keyword) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, ROOT_CATEGORIES_PER_PAGE, sort);
        Page<Category> pageCategories;

        if (keyword != null && !(keyword.isEmpty())) {
            pageCategories = categoryRepository.search(keyword, pageable);
        } else {
            pageCategories = categoryRepository.findRootCategories(pageable);
        }
        List<Category> rootCategories = pageCategories.getContent();
        categoryInfor.setTotalElements(pageCategories.getTotalElements());
        categoryInfor.setTotalPages(pageCategories.getTotalPages());

        if (keyword != null && !(keyword.isEmpty())) {
            List<Category> searchResult = pageCategories.getContent();
            for (Category category : searchResult) {
                category.setHasChildren(searchResult.size() > 0);
            }
            return searchResult;
        } else {
            return listHierarchicalCategories(rootCategories, sortDir);
        }
    }

    // FIXME: 3/14/2022 Honestly, I have to come back at this code.
    public List<Category> listAllCategoriesInForm() {
        List<Category> categoriesInform = new ArrayList<>();
        Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(sortByName);

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesInform.add(Category.copyIdAndName(category));

                Set<Category> children = sortSubCategories(category.getChildren());
                for (Category subCategory : children) {
                    String format = "--" + subCategory.getName();
                    categoriesInform.add(
                            Category.copyIdAndName(subCategory.getId(), format));
                    listSubCategoriesUsedInForm(categoriesInform, subCategory, 1);
                }
            }
        }
        return categoriesInform;
    }

    private List<Category> listHierarchicalCategories(
            List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);
            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));
                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }

        }
        return hierarchicalCategories;
    }

    private void listSubCategoriesUsedInForm(
            List<Category> categoriesInform,
            Category parent, int subLevel) {

        int newSubLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren());

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            categoriesInform.add(
                    Category.copyIdAndName(subCategory.getId(), name));
            listSubCategoriesUsedInForm(categoriesInform, subCategory, newSubLevel);
        }
    }

    private void listSubHierarchicalCategories(
            List<Category> hierarchicalCategories,
            Category parent, int subLevel, String sortDir) {

        int newSubLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            hierarchicalCategories.add(Category.copyFull(subCategory, name));
            listSubHierarchicalCategories(hierarchicalCategories,
                    subCategory, newSubLevel, sortDir);
        }
    }

    // TODO Save Category Logic
    public Category save(Category category) {
        Category parent = category.getParent();
        if (parent !=null){
            String allParentIds=parent.getAllParentIDs()==null ? "-" : parent.getAllParentIDs();
            allParentIds +=String.valueOf(parent.getId()) + "-";
            category.setAllParentIDs(allParentIds);
        }
        return categoryRepository.save(category);
    }

    public Category getUserbyId(Integer id) throws CategoryNotFoundException {

        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new CategoryNotFoundException("Sorry! Couldn't find any category with the Id of: " + id);
        }
    }

    public void deleteCategory(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Sorry! Couldn't find any category with the Id of: "
                    + id);
        }
        categoryRepository.deleteById(id);
    }

    public String checkUniqueCategory(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = categoryRepository.findByName(name);
        Category categoryByAlias = categoryRepository.findByAlias(alias);

        if (isCreatingNew) {
            if (categoryByName != null) {
                return "DuplicateName";
            } else {
                if (categoryByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (categoryByName != null && !Objects.equals(categoryByName.getId(), id)) {
                return "DuplicateName";
            }
            if (categoryByAlias != null && !Objects.equals(categoryByAlias.getId(), id)) {
                return "DuplicateAlias";
            }
        }
        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    // Using sorted Set to sort sub-categories.
    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if (sortDir.equals("asc")) {
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });
        sortedChildren.addAll(children);
        return sortedChildren;
    }

    public void updateEnabled(Integer id, boolean enabled) {
        categoryRepository.updateEnabledCategory(id, enabled);
    }
}
